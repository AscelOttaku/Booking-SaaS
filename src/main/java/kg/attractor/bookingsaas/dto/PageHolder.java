package kg.attractor.bookingsaas.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PageHolder<T> {
    private List<T> content;
    private Integer page;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
    private Boolean hasNextPage;
    private Boolean hasPreviousPage;
}
