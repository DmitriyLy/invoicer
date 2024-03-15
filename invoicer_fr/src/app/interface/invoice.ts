export interface Invoice {
  id: number;
  number: string;
  services: string;
  status: string;
  total: number;
  date: Date;
}
