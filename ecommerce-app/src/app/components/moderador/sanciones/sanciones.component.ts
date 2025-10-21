// src/app/pages/moderador/gestion-sanciones/gestion-sanciones.component.ts
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModeracionService } from '../../../services/moderacion/moderacion.service';
import { SancionView } from '../../../entities/SancionView';

@Component({
  standalone: true,
  selector: 'app-sanciones',
  imports: [CommonModule, FormsModule],
  templateUrl: './sanciones.component.html'
})
export class SancionesComponent implements OnInit {
  usuarioId?: number;
  motivo = '';
  sanciones: SancionView[] = [];
  estadoFiltro: 'ACTIVA'|'LEVANTADA'|'' = 'ACTIVA';
  cargando = false;

  constructor(private mod: ModeracionService) {}

  vendedores: {id:number; nombre:string; correo:string}[] = [];
  qVend = '';

  ngOnInit(){
    this.buscar();
    this.cargarVendedores();
  }

  cargarVendedores() {
    this.mod.listarVendedores(this.qVend).subscribe(v => this.vendedores = v);
  }

  crear() {
    if (!this.usuarioId || !this.motivo.trim()) return;
    this.mod.crearSancion(this.usuarioId, this.motivo).subscribe(_ => {
      this.motivo = ''; this.buscar();
    });
  }

  levantar(id: number) {
    this.mod.levantarSancion(id).subscribe(_ => this.buscar());
  }

  buscar() {
    this.cargando = true;
    this.mod.listarSanciones(this.usuarioId, this.estadoFiltro || undefined)
      .subscribe(data => { this.sanciones = data; this.cargando = false; });
  }
}
