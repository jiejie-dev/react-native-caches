import React, { useEffect, useState } from 'react'
import { View, Text, Button } from 'react-native'
import Caches from 'react-native-caches'

const App = () => {
  const [cacheSize, setCacheSize] = useState('0')
  const [cacheUnit, setCacheUnit] = useState('')

  const fetchInfo = async () => {
    const info = await Caches.getCacheSize()
    console.log('fetchInfo', info)
    setCacheSize(info.cacheSize)
    setCacheUnit(info.unit)
  }

  useEffect(() => {
    fetchInfo()
  }, [])

  const clearCache = async () => {
    await Caches.runClearCache()
    await fetchInfo()
  }

  return (
    <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
      <Text style={{ fontSize: 20, textAlign: 'center', margin: 10 }}>
        缓存大小{cacheSize}
        {cacheUnit}
      </Text>
      <Button title='清除缓存' onPress={clearCache} />
    </View>
  )
}

export default App
