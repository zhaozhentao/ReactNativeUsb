import Usb from './NativeUsb';

export function connect(): string {
  return Usb.connect();
}

export function send(): void {
  return Usb.send();
}
