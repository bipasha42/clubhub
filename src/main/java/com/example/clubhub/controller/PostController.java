@GetMapping("/approved")
public String listApprovedPosts(Model model) {
    model.addAttribute("posts", postRepository.findAll().stream()
        .filter(Post::isApproved)
        .toList());
    return "approved_posts"; // This will use approved_posts.html
}