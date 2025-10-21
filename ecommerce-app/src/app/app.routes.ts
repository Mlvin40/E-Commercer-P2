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

// COMÚN
import { ProductoFormComponent } from './components/comun/producto-form/producto-form.component';
import { CatalogoComponent } from './components/comun/catalogo/catalogo.component';
import { MisProductosComponent } from './components/comun/mis-productos/mis-productos.component';
import { CarritoComponent } from './components/comun/carrito/carrito.component';
import { PedidosComponent } from './components/comun/pedidos/pedidos.component';

// MODERADOR
import { RevisionesComponent } from './components/moderador/revisiones/revisiones.component';
import { SancionesComponent } from './components/moderador/sanciones/sanciones.component';

// ADMIN
import { HistorialNotificacionesComponent } from './components/admin/historial-notificaciones/historial-notificaciones.component';
import { ProductosMasVendidosComponent } from './components/admin/productos-mas-vendidos/productos-mas-vendidos.component';
import { TopClientesGananciaComponent } from './components/admin/top-clientes-ganancia/top-clientes-ganancia.component';
import { TopClientesVentasComponent } from './components/admin/top-clientes-ventas/top-clientes-ventas.component';
import { ClientesProductosVentaComponent } from './components/admin/clientes-productos-venta/clientes-productos-venta.component';
import { HistorialSancionesComponent } from './components/admin/historial-sanciones/historial-sanciones.component';
import { RegistroUsuarioComponent } from './components/admin/registro-usuario/registro-usuario.component';
import { TopClientesPedidosComponent } from './components/admin/top-clientes-pedidos/top-clientes-pedidos.component';


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
      { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
      { path: 'vender', component: ProductoFormComponent },
      { path: 'catalogo', component: CatalogoComponent },
      { path: 'mis-productos', component: MisProductosComponent },
      { path: 'carrito', component: CarritoComponent },
      { path: 'pedidos', component: PedidosComponent },
    ]
  },

  {
    path: 'adminHome',
    component: AdminHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'ADMIN' },
    children: [
      { path: 'historialNotificaciones', component: HistorialNotificacionesComponent },
      { path: 'productosMasVendidos', component: ProductosMasVendidosComponent },
      { path: 'topClientesGanancia', component: TopClientesGananciaComponent },
      { path: 'topClientesVentas', component: TopClientesVentasComponent },
      { path: 'clientesProductosVenta', component: ClientesProductosVentaComponent },
      { path: 'historialSanciones', component: HistorialSancionesComponent },
      { path: 'registroUsuario', component: RegistroUsuarioComponent },
      { path: 'topClientesPedidos', component: TopClientesPedidosComponent },
    ]
  },

  // MODERADOR (placeholder)
  {
    path: 'moderadorHome',
    component: ModeradorHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'MODERADOR' },
    children: [
      { path: '', redirectTo: 'gestionRevisiones', pathMatch: 'full' },
      { path: 'gestionRevisiones', component: RevisionesComponent },
      { path: 'gestionSanciones', component: SancionesComponent },
    ]
  },

  // LOGÍSTICA (placeholder)
  {
    path: 'logisticaHome',
    component: LogisticaHomeComponent,
    canActivate: [AuthGuard],
    data: { role: 'LOGISTICA' },
    children: [
      { path: 'logisticaHome', component: LogisticaHomeComponent },
    ]
  },

  // 404 para los demás casos
  { path: '**', redirectTo: 'inicio' }
];
