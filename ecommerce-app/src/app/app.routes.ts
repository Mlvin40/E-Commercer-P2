// app.routes.ts
import { Routes } from '@angular/router';
import { InicioComponent } from './components/portal/inicio/inicio.component';
import { LoginComponent } from './components/portal/login/login.component';
import { RegistroComponent } from './components/portal/registro/registro.component';
import { AuthGuard } from './core/auth.guard';

import { ComunHomeComponent } from './components/comun/comun-home/comun-home.component';
import { AdminHomeComponent } from './components/admin/admin-home/admin-home.component';
import { ModeradorHomeComponent } from './components/moderador/moderador-home/moderador-home.component';
import { LogisticaHomeComponent } from './components/logistica/logistica-home/logistica-home.component';

// SUBVISTAS COMÚN
import { ProductoFormComponent } from './components/comun/producto-form/producto-form.component';
import { CatalogoComponent } from './components/comun/catalogo/catalogo.component';
import { MisProductosComponent } from './components/comun/mis-productos/mis-productos.component';
import { CarritoComponent } from './components/comun/carrito/carrito.component';
import { PedidosComponent } from './components/comun/pedidos/pedidos.component';

// (para admin/moderador/logística puedes crear luego sus subrutas)
export const routes: Routes = [
  { path: 'inicio', component: InicioComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registro', component: RegistroComponent },
  { path: '', redirectTo: 'inicio', pathMatch: 'full' },

  // COMÚN (con children)
  {
    path: 'comunHome',
    component: ComunHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'COMUN' },
    children: [
      { path: '', redirectTo: 'vender', pathMatch: 'full' },
      { path: 'vender', component: ProductoFormComponent },
      { path: 'catalogo', component: CatalogoComponent },
      { path: 'mis-productos', component: MisProductosComponent },
      { path: 'carrito', component: CarritoComponent },
      { path: 'pedidos', component: PedidosComponent },
    ]
  },

  // ADMIN (placeholder, luego agregas children)
  {
    path: 'adminHome',
    component: AdminHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' },
    children: [
      // { path: 'usuarios', component: AdminUsuariosComponent },
      // { path: 'reportes', component: AdminReportesComponent },
    ]
  },

  // MODERADOR (placeholder)
  {
    path: 'moderadorHome',
    component: ModeradorHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'MODERADOR' },
    children: [
      // { path: 'revisiones', component: RevisionesComponent },
      // { path: 'sanciones', component: SancionesComponent },
    ]
  },

  // LOGÍSTICA (placeholder)
  {
    path: 'logisticaHome',
    component: LogisticaHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'LOGISTICA' },
    children: [
      // { path: 'pedidos', component: LogisticaPedidosComponent },
    ]
  },

  // 404 opcional
  { path: '**', redirectTo: 'inicio' }
];
