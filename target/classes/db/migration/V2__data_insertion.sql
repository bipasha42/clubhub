DO $$
DECLARE
  -- Reference data
  country_names text[] := ARRAY[
    'United States','Canada','United Kingdom','Germany','India',
    'Australia','Brazil','Japan','France','South Africa'
  ];
  club_names text[] := ARRAY[
    'Coding Club','Robotics Society','Photography Society','Music Ensemble','Drama Club','Debate Union',
    'Entrepreneurship Cell','Environmental Club','AI & ML Club','Gaming Guild','Literature Circle','Film Society',
    'Mathematics Society','Physics Association','Chemistry Collective','Astronomy Club','Art & Design Club',
    'Finance & Investment Club','Sports & Fitness Club','Volunteer & Outreach'
  ];
  fnames text[] := ARRAY[
    'Alex','Sam','Jordan','Taylor','Casey','Jamie','Riley','Avery','Morgan','Cameron','Drew','Skyler','Quinn',
    'Peyton','Dakota','Reese','Kendall','Bailey','Harper','Rowan','Logan','Mason','Olivia','Liam','Emma','Noah',
    'Sophia','Ethan','Isabella','Aiden','Mia','Lucas','Amelia','James','Charlotte','William','Evelyn','Benjamin',
    'Abigail','Elijah','Emily'
  ];
  lnames text[] := ARRAY[
    'Smith','Johnson','Williams','Brown','Jones','Miller','Davis','Garcia','Rodriguez','Wilson','Martinez',
    'Anderson','Taylor','Thomas','Hernandez','Moore','Martin','Jackson','Thompson','White','Lopez','Lee',
    'Gonzalez','Harris','Clark','Lewis','Young','Walker','Hall','Allen','Sanchez','Wright','King','Scott',
    'Green','Baker','Adams','Nelson','Hill','Ramirez'
  ];

  -- CONFIG (tweak sizes as needed)
  universities_per_country int := 5;  -- >= 1
  clubs_per_university     int := 6;  -- >= 4
  students_per_university  int := 120;
  members_per_club         int := 40;
  apps_per_club            int := 8;
  posts_per_club           int := 5;  -- >= 3
  comments_per_post        int := 3;
  likes_per_post           int := 12;

  -- Working vars (prefixed to avoid column-name collisions)
  v_country text;
  v_country_id integer;
  v_uni_name text;
  v_uni_id uuid;
  v_uni_admin_id uuid;
  v_uni_domain text;
  u_idx int := 0;
  v_first text;
  v_last text;
  v_email text;
  v_pwd text;

  v_student_ids uuid[];
  v_student_id uuid;

  v_club_admin_id uuid;
  v_club_id uuid;
  cj int;
  v_club_member_ids uuid[];
  v_start_idx int;
  v_start_app_idx int;

  s int; p int; k int; m int; a int; i int;
  v_post_id uuid;
  v_applicant_id uuid;
  v_app_id uuid;
  v_status text;
  v_processed_ts timestamptz;
  v_like_user uuid;
  v_comment_user uuid;
BEGIN
  FOREACH v_country IN ARRAY country_names LOOP
    INSERT INTO Country (name)
    VALUES (v_country)
    ON CONFLICT (name) DO UPDATE SET name = EXCLUDED.name
    RETURNING id INTO v_country_id;

    FOR i IN 1..universities_per_country LOOP
      u_idx := u_idx + 1;
      v_uni_name := 'University of ' || v_country || ' ' || i;

      INSERT INTO University (name, country_id)
      VALUES (v_uni_name, v_country_id)
      ON CONFLICT (name, country_id) DO UPDATE SET name = EXCLUDED.name
      RETURNING id INTO v_uni_id;

      v_uni_domain := 'u' || lpad(u_idx::text, 3, '0') || '.edu';

      -- One UNIVERSITY_ADMIN per university
      v_first := fnames[1 + ((u_idx + i) % array_length(fnames, 1))];
      v_last  := lnames[1 + ((u_idx + i) % array_length(lnames, 1))];
      v_email := lower(v_first) || '.' || lower(v_last) || '.ua' || u_idx || '@admin.' || v_uni_domain;
      v_pwd   := 'ua' || u_idx;

      INSERT INTO "User" (email, password_hash, first_name, last_name, role, university_id, bio)
      VALUES (v_email, v_pwd, v_first, v_last, 'UNIVERSITY_ADMIN', v_uni_id, 'University admin for ' || v_uni_name)
      ON CONFLICT (email) DO UPDATE SET email = EXCLUDED.email
      RETURNING id INTO v_uni_admin_id;

      -- Students
      v_student_ids := ARRAY[]::uuid[];
      FOR s IN 1..students_per_university LOOP
        v_first := fnames[1 + ((s + u_idx) % array_length(fnames, 1))];
        v_last  := lnames[1 + ((s + u_idx) % array_length(lnames, 1))];
        v_email := lower(v_first) || '.' || lower(v_last) || '.' || s || '@students.' || v_uni_domain;
        v_pwd   := 's' || ((s % 900) + 100);

        INSERT INTO "User" (email, password_hash, first_name, last_name, role, university_id)
        VALUES (v_email, v_pwd, v_first, v_last, 'STUDENT', v_uni_id)
        ON CONFLICT (email) DO UPDATE SET email = EXCLUDED.email
        RETURNING id INTO v_student_id;

        v_student_ids := array_append(v_student_ids, v_student_id);
      END LOOP;

      -- Clubs and their club admins (one admin per club)
      FOR cj IN 1..clubs_per_university LOOP
        v_first := fnames[1 + ((cj + u_idx) % array_length(fnames, 1))];
        v_last  := lnames[1 + ((cj + u_idx) % array_length(lnames, 1))];
        v_email := 'ca' || u_idx || '_' || cj || '@clubs.' || v_uni_domain;
        v_pwd   := 'ca' || cj;

        INSERT INTO "User" (email, password_hash, first_name, last_name, role, university_id, bio)
        VALUES (v_email, v_pwd, v_first, v_last, 'CLUB_ADMIN', v_uni_id, 'Club admin at ' || v_uni_name)
        ON CONFLICT (email) DO UPDATE SET email = EXCLUDED.email
        RETURNING id INTO v_club_admin_id;

        INSERT INTO Club (name, description, university_id, admin_id, logo_url, banner_url)
        VALUES (
          club_names[1 + ((cj - 1) % array_length(club_names, 1))],
          'Welcome to ' || club_names[1 + ((cj - 1) % array_length(club_names, 1))] || ' at ' || v_uni_name || '!',
          v_uni_id,
          v_club_admin_id,
          'https://picsum.photos/seed/logo-u' || u_idx || '-c' || cj || '/200',
          'https://picsum.photos/seed/banner-u' || u_idx || '-c' || cj || '/1200/300'
        )
        ON CONFLICT (name, university_id) DO UPDATE SET description = EXCLUDED.description
        RETURNING id INTO v_club_id;

        -- Members and follows
        v_club_member_ids := ARRAY[]::uuid[];
        v_start_idx := ((cj - 1) * members_per_club) % students_per_university + 1;
        FOR m IN 0..(members_per_club - 1) LOOP
          v_student_id := v_student_ids[((v_start_idx - 1 + m) % students_per_university) + 1];

          INSERT INTO ClubMember (club_id, user_id)
          VALUES (v_club_id, v_student_id)
          ON CONFLICT (club_id, user_id) DO NOTHING;

          INSERT INTO Follow (club_id, user_id)
          VALUES (v_club_id, v_student_id)
          ON CONFLICT (club_id, user_id) DO NOTHING;

          v_club_member_ids := array_append(v_club_member_ids, v_student_id);
        END LOOP;

        -- Applications + notifications
        v_start_app_idx := ((v_start_idx - 1 + members_per_club) % students_per_university) + 1;
        FOR a IN 0..(apps_per_club - 1) LOOP
          v_applicant_id := v_student_ids[((v_start_app_idx - 1 + a) % students_per_university) + 1];

          IF a < 3 THEN
            v_status := 'ACCEPTED';
          ELSIF a < 5 THEN
            v_status := 'REJECTED';
          ELSE
            v_status := 'PENDING';
          END IF;

          IF v_status IN ('ACCEPTED','REJECTED') THEN
            v_processed_ts := now() - (floor(random() * 30))::int * interval '1 day';
          ELSE
            v_processed_ts := NULL;
          END IF;

          v_app_id := NULL;
          INSERT INTO ClubApplication (club_id, user_id, application_text, status, processed_at, processed_by)
          VALUES (
            v_club_id,
            v_applicant_id,
            'I would love to join ' || club_names[1 + ((cj - 1) % array_length(club_names, 1))] || ' at ' || v_uni_name || '.',
            v_status,
            v_processed_ts,
            CASE WHEN v_status IN ('ACCEPTED','REJECTED') THEN v_club_admin_id ELSE NULL END
          )
          ON CONFLICT (club_id, user_id) DO NOTHING
          RETURNING id INTO v_app_id;

          IF v_app_id IS NOT NULL AND v_status IN ('ACCEPTED','REJECTED') THEN
            INSERT INTO Notification (application_id, recipient_id, message, is_read)
            VALUES (
              v_app_id,
              v_applicant_id,
              CASE
                WHEN v_status = 'ACCEPTED' THEN 'Your application to ' || club_names[1 + ((cj - 1) % array_length(club_names, 1))] || ' has been accepted.'
                ELSE 'Your application to ' || club_names[1 + ((cj - 1) % array_length(club_names, 1))] || ' has been rejected.'
              END,
              false
            );
          END IF;
        END LOOP;

        -- Posts + comments + likes
        FOR p IN 1..posts_per_club LOOP
          INSERT INTO Post (club_id, author_id, content)
          VALUES (
            v_club_id,
            CASE
              WHEN (p % 2) = 1 THEN v_club_admin_id
              ELSE v_club_member_ids[((p - 1) % array_length(v_club_member_ids, 1)) + 1]
            END,
            'Post #' || p || ' from ' || club_names[1 + ((cj - 1) % array_length(club_names, 1))] || ' at ' || v_uni_name || ' (u' || u_idx || ', c' || cj || ').'
          )
          RETURNING id INTO v_post_id;

          -- Comments
          FOR k IN 1..comments_per_post LOOP
            v_comment_user := v_club_member_ids[((p - 1) * comments_per_post + k - 1) % array_length(v_club_member_ids, 1) + 1];
            INSERT INTO PostComment (post_id, author_id, content)
            VALUES (v_post_id, v_comment_user, 'Comment #' || k || ' on post #' || p || '.');
          END LOOP;

          -- Likes
          FOR k IN 1..likes_per_post LOOP
            v_like_user := v_club_member_ids[((p - 1) * likes_per_post + k - 1) % array_length(v_club_member_ids, 1) + 1];
            INSERT INTO PostLikes (post_id, user_id)
            VALUES (v_post_id, v_like_user)
            ON CONFLICT (post_id, user_id) DO NOTHING;
          END LOOP;
        END LOOP;

      END LOOP; -- clubs per university
    END LOOP; -- universities per country
  END LOOP; -- countries
END $$ LANGUAGE plpgsql;