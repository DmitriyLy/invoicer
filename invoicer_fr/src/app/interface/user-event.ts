import {EventType} from "../enum/event-type";

export interface UserEvent {
  id: number;
  type: EventType;
  description: string;
  device: string;
  ipAddress: string;
  createdAt: Date;
}
