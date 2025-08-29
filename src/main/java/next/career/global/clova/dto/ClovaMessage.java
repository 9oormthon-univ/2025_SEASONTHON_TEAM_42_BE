package next.career.global.clova.dto;

public record ClovaMessage(
        String role,
        String content
) {
    public static ClovaMessage of(String role, String content) {
        return new ClovaMessage(role, content);
    }
}