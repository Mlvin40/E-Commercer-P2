export interface SancionView {
  id: number;
  usuarioId: number;
  usuarioNombre: string;
  moderadorId: number;
  moderadorNombre: string;
  motivo: string;
  fecha: string;
  estado: 'ACTIVA'|'LEVANTADA';
}
