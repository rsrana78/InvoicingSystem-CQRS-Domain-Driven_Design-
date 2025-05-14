package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.TransactionDomain;
import com.xcelerate.invoicing.dto.command.response.TransactionCommandDto;
import com.xcelerate.invoicing.dto.query.response.TransactionQueryDto;
import com.xcelerate.invoicing.entity.TransactionEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDomain map(TransactionEntity entity);
    TransactionCommandDto mapToDto(TransactionDomain domain);
    List<TransactionQueryDto> domainToQueryDtoList(List<TransactionDomain> transactionDomainList);
}
