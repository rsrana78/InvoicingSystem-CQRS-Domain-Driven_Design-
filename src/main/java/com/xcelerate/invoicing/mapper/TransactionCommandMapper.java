package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.Transaction;
import com.xcelerate.invoicing.dto.command.response.TransactionCommandDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TransactionCommandMapper {
    TransactionCommandDto toDto(Transaction transaction);
}
