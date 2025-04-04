package codesquad.codestagram.common.dto;

public record PaginationDto(
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static PaginationDto of(int page, int size, long totalElements) {
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PaginationDto(page, size, totalElements, totalPages);
    }
}
