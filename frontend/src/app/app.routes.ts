import { Routes } from '@angular/router';

import { Home } from './home/home';
import { ProductHome } from './product-home/product-home';
import { ProductList } from './product-list/product-list';
import { ProductEdit } from './product-edit/product-edit';

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

  { path: '**', redirectTo: '' }
];
