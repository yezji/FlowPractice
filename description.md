# Study Flow
목표 : StateFlow, SharedFlow, Channel, ChannelFlow 데이터 방출 시점 차이 알기

## 화면 동작 순서
- MainActivity -> SubActivity(종료 시 특정 값 넘김) -> MainActivity
- NewFragment(MainActivity)-> NewActivity(종료 시 특정 값 넘김) -> NewFragment(MainActivity)


## open SubActivity
channel as flow
- 다른 CoroutineScope를 사용하더라도 Flow이기에 View(MainActivity)에서 정상적으로 값을 받아 출력한다.

## open NewActivity
channel
- 같은 CoroutineScope를 사용해야 방출된 값을 받아올 수 있다. (1:1 매칭 영향)
  - 다른 CoroutineScope를 사용했을 때는 값을 받아볼 수 없다.
    - ex. View단에서 channel을 조작하는 것(lifecycleScope.launch)과 ViewModel에서 조작하는 것(viewModelScope.launch)의 범위는 다르다.