class Viper
  constructor: ->
    @version = "0.0.1"
    @canvas = $('#canvas')
    @context = @canvas.get(0).getContext '2d'
    @gamerunning = false

    $('#version').text @version + " by Björn Harrtell"

  init: (response) =>

    @context.clearRect 0, 0, @canvas.width(), @canvas.height()
  
  create: ->
    $.ajax
      url: "/#{@urls.games}"
      type: 'POST'
      success: @joingame

  join: ->
    $.ajax
      url: "/#{@urls.games}/random"
      success: @joingame
      
    
  onGameover: (data) =>
    clearInterval @intervalID
    @gamerunning = false
    $(document).unbind 'keydown', @onKeydown
    $(document).unbind 'keyup', @onKeup
    $(document).unbind 'mousedown', @onMousedown
    $(document).unbind 'mouseup', @onMouseup
    $(document).unbind 'touchstart', @onTouchstart
    $(document).unbind 'touchend', @onTouchend
    @worms = []
  
    if data is 0
      @progress.text 'Draw!'
    else if data is 1
      @progress.text 'You won!' 
    else if data is 2
      @progress.text 'You lost!'
    else if data is 3
      @progress.text 'Opponent disconnected!'
 
    @progress.fadeIn()
    
    @sounds.gameover.play()
    
    setTimeout @initmenu, 3000

  startgame: =>
    @gamerunning = true
  
    @context.clearRect 0, 0, @canvas.width(), @canvas.height()
  
    $(document).bind 'keydown', @onKeydown
    $(document).bind 'keyup', @onKeyup
    $(document).bind 'mousedown', @onMousedown
    $(document).bind 'mouseup', @onMouseup
    $(document).bind 'touchstart', @onTouchstart
    $(document).bind 'touchend', @onTouchend
    @score.fadeIn()
    @sounds.start.play()
    @sounds.wohoo.play()
    x = Math.random() * 0.6 + 0.2;
    y = Math.random() * 0.6 + 0.2;
    direction = (Math.random() - 0.5) * 2.0 * Math.PI;
    worm = new Worm new jsts.geom.Coordinate(x, y), direction
    @worms.push worm

    now = new Date().getTime()
    @lastTime = now + 100
    
    # TODO: optimize with requestAnimationFrame
    @intervalID = setInterval @timestep, 50

  timestep: =>
    now = new Date().getTime()

    if @lastTime > now then return false

    @elapsed = now - @lastTime

    worm = @worms[0]
    
    if worm.alive
      move = worm.move @elapsed
            
      if move.wallCollision then @sounds.bounce.play()
      worm.draw @context
      @collisionTest worm
      
      # report move to server which will send it to other player(s)
      if @socket
        @socket.emit 'move', 
          sessionID: @sessionID
          gameID: @gameID
          x: move.x
          y: move.y
          hole: move.hole
          alive: worm.alive
          score: worm.score
    
    @score.text "Score: #{worm.score}"

    @lastTime = now

    return true

  collisionTest: (worm) ->
    for otherWorm in @worms
      result = otherWorm.collisionTest worm.segments[worm.segments.length-1]
      
      if result is 2
        worm.alive = false
        dohIndex = Math.floor Math.random() * 6
        @sounds.doh[dohIndex].play()
        if @worms.length is 1 then @onGameover()
      else if result is 1 
        worm.score += 1
        @sounds.thread.play()

  onKeydown: (e) =>
    e.preventDefault()
    if e.keyCode is 37
      @worms[0].torque = -0.002
    else if e.keyCode is 39
      @worms[0].torque = 0.002

  onKeyup: (e) =>
    e.preventDefault();
    if e.keyCode is 37 or e.keyCode is 39
      @worms[0].torque = 0
      
  onMousedown: (e) =>
    e.preventDefault()
    
    if e.clientX < $(document).width()/2
      @worms[0].torque = -0.002
    else
      @worms[0].torque = 0.002
    
  onMouseup: (e) =>
    e.preventDefault()

    @worms[0].torque = 0
    
  onTouchstart: (e) =>
    e.preventDefault()
  
    if not @isTouch
      $(document).unbind 'mousedown', @onMousedown
      $(document).unbind 'mouseup', @onMouseup
      @isTouch = true
      
    if e.originalEvent.touches.length isnt 1
      return false
    
    touch = e.originalEvent.touches[0]
    
    if touch.pageX < $(document).width()/2
      @worms[0].torque = -0.002
    else
      @worms[0].torque = 0.002
    
  onTouchend: (e) =>
    e.preventDefault()

    @worms[0].torque = 0
    
new Viper()

