import Usb from './NativeUsb';

export function connect(): string {
  return Usb.connect();
}
