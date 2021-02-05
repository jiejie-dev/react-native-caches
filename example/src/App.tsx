import React, { useEffect } from 'react'
import RNCaches, { Counter } from 'react-native-caches'

const App = () => {
  useEffect(() => {
    console.log(RNCaches)
  })

  return <Counter />
}

export default App
