import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; 
import { CategoryService } from '../services/category.service';
import { Category} from '../model/category.model';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category-list.html'
 
})
export class CategoryList implements OnInit{
    categories: Category[] = [];
    allCategories: Category[] = [];
    searchText: string = '';
    loading: boolean = false;

    constructor(
    private categoryService: CategoryService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  private loadCategories(): void {
  this.loading = true;

  this.categoryService.getAll().subscribe({
      next: (data) => {
        this.categories = data;
        this.allCategories = data;
        this.loading = false;
        this.cdr.detectChanges(); // 🔥 CLAVE PARA QUE SE RENDERICE
      },
      error: (err) => {
        console.error(err);
        this.loading = false;
      }
    });
  }

  goBackToCategories(): void {
    this.router.navigate(['/categories']);
  }

    
  search(): void {
    if (!this.searchText || this.searchText.trim() === '') {
      this.loadCategories();
      return;
    }

    this.categoryService.searchByName(this.searchText.trim())
      .subscribe({
        next: (data) => {
          this.categories = data;   // ✅ data YA ES Category[]
        },
        error: () => {
          this.categories = [];
          alert('No se encontraron categorías');
        }
      });
  }



  clearSearch(): void {
    this.searchText = '';
    this.loadCategories();
  }

  editCategory(id: number): void {
    this.router.navigate(['/categories/edit', id]);
  }

  deleteCategory(id: number): void {
  if (!confirm('¿Seguro que deseas eliminar esta categoría?')) return;

  this.categoryService.delete(id).subscribe({
    next: () => {
      alert('Categoría eliminada correctamente');
      this.loadCategories();
    },
    error: (err) => {
      alert('No se puede eliminar la categoría porque tiene productos asociados');
    }
  });
}



}
