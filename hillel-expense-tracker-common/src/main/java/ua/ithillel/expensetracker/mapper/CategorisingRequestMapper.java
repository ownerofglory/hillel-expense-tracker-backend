package ua.ithillel.expensetracker.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import ua.ithillel.expensetracker.dto.CategorisingRequestDTO;
import ua.ithillel.expensetracker.model.CategorisingRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategorisingRequestMapper {
    CategorisingRequestMapper INSTANCE = Mappers.getMapper(CategorisingRequestMapper.class);

    CategorisingRequestDTO requestToDto(CategorisingRequest categorisingRequest);
    CategorisingRequest dtoToRequest(CategorisingRequestDTO categorisingRequestDTO);
}
