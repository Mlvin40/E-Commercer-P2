import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ModeracionService} from '../../../services/moderacion/moderacion.service';
import { ProductoPendienteView } from '../../../entities/ProductoPendienteView';
import { FormsModule } from '@angular/forms';

@Component({
  standalone: true,
  selector: 'app-moderacion-lista',
  imports: [CommonModule, FormsModule],
  templateUrl: './moderacion-lista.component.html'
})
export class ModeracionListaComponent implements OnInit {
  items: ProductoPendienteView[] = [];
  cargando = false;
  error: string | null = null;
  comentario: string = '';

  constructor(private api: ModeracionService) {}

  ngOnInit(): void { this.load(); }

  load() {
    this.error = null; this.cargando = true;
    this.api.pendientes().subscribe({
      next: res => { this.items = res; this.cargando = false; },
      error: err => { this.error = 'No se pudo cargar.'; this.cargando = false; }
    });
  }

  aprobar(id: number) {
    this.api.aprobar(id).subscribe({ next: () => this.load() });
  }

  rechazar(id: number) {
    const c = prompt('Motivo de rechazo (opcional):', '');
    this.api.rechazar(id, c || undefined).subscribe({ next: () => this.load() });
  }
}
