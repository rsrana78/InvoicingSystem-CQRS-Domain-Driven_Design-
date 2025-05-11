package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.Transaction;
import com.xcelerate.invoicing.dto.command.response.TransactionCommandDto;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionQueryMapper {
    TransactionQueryDto toDto(Transaction transaction);
    List<TransactionQueryDto> toDto(List<Transaction> transaction);
}
