import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  connect(): string;

  send(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('Usb');
