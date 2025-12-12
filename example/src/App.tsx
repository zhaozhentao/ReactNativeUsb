import { Text, View, StyleSheet } from 'react-native';
import { connect } from 'react-native-usb';

const device = connect();

export default function App() {
  return (
    <View style={styles.container}>
      <Text>Device: {device}</Text>
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
