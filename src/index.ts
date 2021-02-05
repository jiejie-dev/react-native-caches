import { NativeModules } from 'react-native'

const ClearCacheModuleObj = NativeModules.RNCaches

interface CacheInfo {
  cacheSize: string
  unit: string
}

class Caches {
  async getCacheSize(): Promise<CacheInfo> {
    return ClearCacheModuleObj.getCacheSize()
  }

  async runClearCache(): Promise<void> {
    return ClearCacheModuleObj.runClearCache()
  }
}
export default new Caches()
