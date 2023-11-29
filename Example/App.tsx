import React, {Component, useEffect, useState} from 'react';
import {Button, Text, View, TouchableOpacity} from 'react-native';
import {Image} from 'react-native';
import ImageEditor from '@thienmd/react-native-image-editor';
import RNFS from 'react-native-fs';
import RNFetchBlob from 'react-native-blob-util';
import RTNCalculator from 'rtn-calculator/js/NativeCalculator';

const App = () => {
  const [result, setResult] = useState<number | null>(null);

  const onPress = () => {
    ImageEditor.Edit({
      path: RNFS.DocumentDirectoryPath + '/photo.jpg',
      stickers: [
        'sticker0',
        'sticker1',
        'sticker2',
        'sticker3',
        'sticker4',
        'sticker5',
        'sticker6',
        'sticker7',
        'sticker8',
        'sticker9',
        'sticker10',
      ],
      colors: undefined,
      onDone: () => {
        console.log('on done');
      },
      onCancel: () => {
        console.log('on cancel');
      },
    });
  };

  useEffect(() => {
    let photoPath = RNFS.DocumentDirectoryPath + '/photo.jpg';
    let binaryFile = Image.resolveAssetSource(require('./assets/photo.jpg'));

    RNFetchBlob.config({fileCache: true})
      .fetch('GET', binaryFile.uri)
      .then(resp => {
        RNFS.moveFile(resp.path(), photoPath)
          .then(() => {
            console.log('FILE WRITTEN!');
          })
          .catch(err => {
            console.log(err.message);
          });
      })
      .catch(err => {
        console.log(err.message);
      });
  }, []);

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={onPress}>
        <Text>Click</Text>
      </TouchableOpacity>
      <Text style={{marginLeft: 20, marginTop: 20}}>3+7={result ?? '??'}</Text>
      <Button
        title="Compute"
        onPress={async () => {
          const value = await RTNCalculator?.add(3, 7);
          setResult(value ?? null);
        }}
      />
    </View>
  );
};

export default App;
