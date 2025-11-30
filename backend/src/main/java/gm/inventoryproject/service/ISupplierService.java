package gm.inventoryproject.service;

import gm.inventoryproject.model.Supplier;

import java.util.List;

public interface ISupplierService {

    List<Supplier> getAll();

    Supplier getById(Long id);

    Supplier findByName(String name);

    Supplier create(Supplier supplier);

    Supplier update(Long id, Supplier supplier);

    void delete(Long id);

    Supplier createFromDto(gm.inventoryproject.dto.supplier.SupplierRequestDto dto);
}


