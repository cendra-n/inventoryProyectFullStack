import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { InventoryMovement } from '../model/inventory-movement.model';

export interface InventoryMovementCreateRequest {
  productId: number;
  quantity: number;
  type: 'IN' | 'OUT';
}

@Injectable({
  providedIn: 'root'
})
export class InventoryService {

  private apiUrl = 'http://localhost:8080/inventory-app/movements';

  constructor(private http: HttpClient) {}

  // -------------------------
  // LISTADO
  // -------------------------
  getAll(): Observable<InventoryMovement[]> {
    return this.http.get<InventoryMovement[]>(this.apiUrl);
  }

  getById(id: number): Observable<InventoryMovement> {
    return this.http.get<InventoryMovement>(`${this.apiUrl}/${id}`);
  }

  // -------------------------
  // CREATE (REQUEST DTO)
  // -------------------------
  create(
    movement: InventoryMovementCreateRequest
  ): Observable<InventoryMovement> {
    return this.http.post<InventoryMovement>(this.apiUrl, movement);
  }

  // -------------------------
  // BÚSQUEDAS
  // -------------------------
  getByProduct(productId: number): Observable<InventoryMovement[]> {
    return this.http.get<InventoryMovement[]>(
      `${this.apiUrl}/product/${productId}`
    );
  }

  getByDateRange(start: string, end: string): Observable<InventoryMovement[]> {
    const params = new HttpParams()
      .set('start', start)
      .set('end', end);

    return this.http.get<InventoryMovement[]>(
      `${this.apiUrl}/dates`,
      { params }
    );
  }

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
