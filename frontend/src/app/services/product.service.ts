import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Product } from '../model/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  private apiUrl = 'http://localhost:8080/inventory-app/products';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl);
  }

  create(product: Partial<Product>) {
    return this.http.post<Product>(this.apiUrl, product);
  }

  getById(id: number) {
    return this.http.get<Product>(`${this.apiUrl}/${id}`);
  }


  searchByName(name: string) {
    return this.http.get<Product[]>(
      `${this.apiUrl}/search`,
      { params: { name } }
    );
  }



  update(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, product);
  }

  delete(id: number): Observable<void> {
   return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }


}
