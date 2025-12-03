package gm.inventoryproject.service;

import gm.inventoryproject.dto.supplier.SupplierRequestDto;
import gm.inventoryproject.model.Supplier;

import java.util.List;

public interface ISupplierService {

    List<Supplier> getAll();

    Supplier getById(Long id);

    Supplier findByName(String name);

    Supplier createFromDto(SupplierRequestDto dto);

    Supplier updateFromDto(Long id, SupplierRequestDto dto);

    void delete(Long id);
}
