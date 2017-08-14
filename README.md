# Android_RealtimeFunction

v0.1.0

This is a android view to display a data graph from a data stream in realtime.

<image src="https://raw.githubusercontent.com/anhmiuhv/Android_RealTimeFunction/master/img/demo.gif" width ="270"/>


### How to use:

```
val view = //...RealtimeFunctionView
val manager = view.manager
manager.addPoint(12f)
manager.addPoint(10f)
manager.addPoint(-4f)
manager.addPoint(12f)
```

### Customizable attributes:

```
<declare-styleable name="RealtimeFunctionView">
    <attr name="lineWidth" format="dimension" />
    <attr name="background_color" format="color" />
    <attr name="line_color" format="color" />
    <attr name="interval" format="float" />
</declare-styleable>
```

### Advanced:

##### Custom Interpolator

```
manager.interpolator = Interpolator.Cosine()
//... or
manager.interpolator = Interpolator.Cosine()
```

##### Add the amount of time to get to the data value

```
manager.addPoint(12f, 200) //Take 200 ms to get to this value, default = 1000ms
```

##### Misc
```
view.scrollRate = 50f //change scroll rate, higher is faster
view.refreshRate = 20f // each data point correspond to 20 ms
```


