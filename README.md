# Android_RealtimeFunction

v0.1.0

This is a android view to display a data graph from a data stream in realtime.

![alt text](https://raw.githubusercontent.com/anhmiuhv/Android_RealTimeFunction/master/img/demo.gif)


How to use:

```
val view = //...RealtimeFunctionView
val manager = view.manager
manager.addPoint(12f)
manager.addPoint(10f)
manager.addPoint(-4f)
manager.addPoint(12f)
```

Customizable attributes:

```
<declare-styleable name="RealtimeFunctionView">
    <attr name="lineWidth" format="dimension" />
    <attr name="background_color" format="color" />
    <attr name="line_color" format="color" />
    <attr name="interval" format="float" />
</declare-styleable>
```