export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  address?: string;
  title?: string;
  bio?: string;
  isEnabled: boolean;
  isNonLocked: boolean;
  isUsingMfa: boolean;
  imageUrl?: string;
  createdAt?: Date;
}
