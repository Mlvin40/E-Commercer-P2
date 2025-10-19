import { TipoUsuario } from './TipoUsuario';

export interface Usuario {
  id: number;
  nombre: string;
  correo: string;
  rol: TipoUsuario;   // 'COMUN'|'MODERADOR'|'LOGISTICA'|'ADMIN'
  activo: boolean;
  fecha_creacion: string;
}
