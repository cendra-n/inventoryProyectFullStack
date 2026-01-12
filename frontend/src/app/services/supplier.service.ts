import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Supplier } from '../model/supplier.model';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class SupplierService {

  private apiUrl = 'http://localhost:8080/inventory-app/suppliers';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.apiUrl);
  }
  
  create(supplier: Partial<Supplier>) {
    return this.http.post<Supplier>(this.apiUrl, supplier);
  }
   
  getById(id: number) {
    return this.http.get<Supplier>(`${this.apiUrl}/${id}`);
  }
   
   
  searchByName(name: string): Observable<Supplier[]> {
      return this.http.get<Supplier[]>(
        `${this.apiUrl}/search`,
        { params: { name } }
      );
  }

  update(id: number, supplier: Supplier): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.apiUrl}/${id}`, supplier);
  }
   
  delete(id: number): Observable<void> {
   return this.http.delete<void>(`${this.apiUrl}/${id}`);
   }  


}

