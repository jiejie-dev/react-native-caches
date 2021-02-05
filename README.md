# react-native-caches

A package to get cache size and clear caches.

## Installation

```console
yarn add react-native-caches
```

## Usage

```typescript
import Caches from 'react-native-caches'

const fetchInfo = async () => {
  const info = await Caches.getCacheSize()
  console.log('fetchInfo', info)
  setCacheSize(info.cacheSize)
  setCacheUnit(info.unit)
}

const clearCache = async () => {
  await Caches.runClearCache()
  await fetchInfo()
}
```

## TODO

- Add android support
- Add ios support