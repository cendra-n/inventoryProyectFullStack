import { Routes } from '@angular/router';
import { Home } from './home/home';
import { ProductList } from './product-list/product-list';
import { CategoryList } from './category-list/category-list';
import { SupplierList } from './supplier-list/supplier-list';

export const routes: Routes = [
  { path: '', component: Home },
  { path: 'products', component: ProductList },
  { path: 'categories', component: CategoryList },
  { path: 'suppliers', component: SupplierList }
];

