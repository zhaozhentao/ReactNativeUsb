import Usb from './NativeUsb';

export function connect(): string {
  return Usb.connect();
}

export function send(data: number[]): void {
  return Usb.send(data);
}
