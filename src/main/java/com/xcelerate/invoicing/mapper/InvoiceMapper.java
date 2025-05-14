package com.xcelerate.invoicing.mapper;

import com.xcelerate.invoicing.domain.InvoiceDomain;
import com.xcelerate.invoicing.dto.command.response.InvoiceCommandDto;
import com.xcelerate.invoicing.dto.query.response.InvoiceQueryDto;
import com.xcelerate.invoicing.entity.InvoiceEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    InvoiceDomain map(InvoiceEntity entity);
    InvoiceCommandDto domainToCommandDTO(InvoiceDomain domain);
    InvoiceQueryDto domainToQueryDto(InvoiceDomain domain);
    List<InvoiceQueryDto> domainToQueryDtoList(List<InvoiceDomain> domainList);
}
