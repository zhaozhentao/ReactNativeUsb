import { TurboModuleRegistry, type TurboModule } from 'react-native';

export interface Spec extends TurboModule {
  connect(): string;
}

export default TurboModuleRegistry.getEnforcing<Spec>('Usb');
