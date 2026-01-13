import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { InventoryMovement } from '../model/inventory-movement.model';

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  private apiUrl = 'http://localhost:8080/inventory-app/movements';

  constructor(private http: HttpClient) {}


  getAll(): Observable<InventoryMovement[]> {
    return this.http.get<InventoryMovement[]>(this.apiUrl);
  }

  getById(id: number): Observable<InventoryMovement> {
    return this.http.get<InventoryMovement>(`${this.apiUrl}/${id}`);
  }

  
  create(movement: {
    quantity: number;
    type: 'IN' | 'OUT';
    productId: number;
  }): Observable<InventoryMovement> {
    return this.http.post<InventoryMovement>(this.apiUrl, movement);
  }

  // ---------------------------------------
  // GET BY PRODUCT
  // ---------------------------------------
  getByProduct(productId: number): Observable<InventoryMovement[]> {
    return this.http.get<InventoryMovement[]>(
      `${this.apiUrl}/product/${productId}`
    );
  }

  // ---------------------------------------
  // GET BY DATE RANGE
  // ---------------------------------------
  getByDateRange(start: string, end: string): Observable<InventoryMovement[]> {
    const params = new HttpParams()
      .set('start', start)
      .set('end', end);

    return this.http.get<InventoryMovement[]>(
      `${this.apiUrl}/dates`,
      { params }
    );
  }

  // ---------------------------------------
  // GET BY PRODUCT + DATE RANGE
  // ---------------------------------------
  getByProductAndDate(
    productId: number,
    start: string,
    end: string
  ): Observable<InventoryMovement[]> {

    const params = new HttpParams()
      .set('start', start)
      .set('end', end);

    return this.http.get<InventoryMovement[]>(
      `${this.apiUrl}/product/${productId}/dates`,
      { params }
    );
  }

}

