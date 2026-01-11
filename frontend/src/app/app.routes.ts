import { Routes } from '@angular/router';

import { Home } from './home/home';
import { ProductHome } from './product-home/product-home';
import { ProductList } from './product-list/product-list';
import { ProductEdit } from './product-edit/product-edit';
import { ProductCreate } from './product-create/product-create';
import { CategoryHome } from './category-home/category-home';
import { CategoryList } from './category-list/category-list';
import { CategoryEdit } from './category-edit/category-edit';
import { CategoryCreate } from './category-create/category-create';

export const routes: Routes = [
  { path: '', component: Home },

  {
    path: 'products',
    component: ProductHome
  },

  {
    path: 'products/list',
    component: ProductList
  },
  
  { path: 'products/edit/:id',
     component: ProductEdit 
  },

  {
  path: 'products/create',
  component: ProductCreate
  },

  {
    path: 'categories',
    component: CategoryHome
  },

  { 
    path: 'categories/list',
    component: CategoryList
  },
  
  { 
    path: 'categories/edit',
    component: CategoryEdit
  },

  { 
    path: 'categories/create',
    component: CategoryCreate
  },

  { path: '**', redirectTo: '' }

];
