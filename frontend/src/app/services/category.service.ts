import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Category } from '../model/category.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private apiUrl = 'http://localhost:8080/inventory-app/categories';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Category[]> {
    return this.http.get<Category[]>(this.apiUrl);
  }
  
  create(category: Partial<Category>) {
    return this.http.post<Category>(this.apiUrl, category);
  }
   
  getById(id: number) {
    return this.http.get<Category>(`${this.apiUrl}/${id}`);
  }
   
   
  searchByName(name: string): Observable<Category[]> {
      return this.http.get<Category[]>(
        `${this.apiUrl}/search`,
        { params: { name } }
      );
  }

  update(id: number, category: Category): Observable<Category> {
    return this.http.put<Category>(`${this.apiUrl}/${id}`, category);
  }
   
   
  delete(id: number): Observable<void> {
   return this.http.delete<void>(`${this.apiUrl}/${id}`);
   }






}

