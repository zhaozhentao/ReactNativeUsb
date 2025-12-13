import { useEffect } from 'react';
import { connect, send } from 'react-native-usb';
import {
  Text,
  Button,
  View,
  StyleSheet,
  ToastAndroid,
  NativeEventEmitter,
  NativeModules,
} from 'react-native';

const { MyModule } = NativeModules;

const device = connect();

export default function App() {
  const eventEmitter = new NativeEventEmitter(MyModule);

  ToastAndroid.show('Hello', ToastAndroid.SHORT);

  useEffect(() => {
    const subscription = eventEmitter.addListener(
      'onSerialDataReceive',
      (data) => {
        ToastAndroid.show('收到数据哦哦哦' + data, ToastAndroid.SHORT);
      }
    );

    return () => subscription.remove();
  });

  return (
    <View style={styles.container}>
      <Text>1 Device: {device}</Text>

      <Button title={'sdf'} onPress={send} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
