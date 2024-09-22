;; The first three lines of this file were inserted by DrRacket. They record metadata
;; about the language level of this file in a form that our tools can easily process.
#reader(lib "htdp-intermediate-lambda-reader.ss" "lang")((modname |Homework 9|) (read-case-sensitive #t) (teachpacks ()) (htdp-settings #(#t constructor repeating-decimal #f #t none #f () #f)))
;; Funpaint 2
(require 2htdp/image)
(require 2htdp/universe)
(define BACKGROUND (empty-scene 400 400))


;; Task 1

;;;;; This function is compatible with Funpaint Part 1, not Funpaint Part 2. The mouse handler that is
;;;;; compatible with FP2 is called "click-check", found at the bottom.

;; shape-mouse : WorldState PosReal PosReal MouseEvent -> WorldState
;; Purpose: Handler for Big Bang. Updates the worldstate with a new shape when the mouse is clicked
;(define (shape-mouse fpws x y event)
;  (cond
;    [(mouse=? event "button-down")
;     (make-fp-ws (fp-ws-placed-shape fpws)
;                 (cons (make-placed-shape (placed-shape-shape (fp-ws-placed-shape fpws))
;                                          (make-posn x y))
;                       (fp-ws-lops fpws)))]
;    [else fpws]))
;; No check expects for shape-mouse since it is not compatible with funpaint part 2.
;; Check expects for the compatible mouse handler found under click-check


;; Task 2

(define-struct button [name type posntl posnbr])
;; A Button is a (make-button Image Position Position)
;; Interpretation: Represents a button where
;; - _type_ is the type of button it is
;; - _posntl_ is the top left coordinate position of the button
;; - _posnbr_ is the bottom right coordinate position of the button
;; Template:
;; b-templ: Button -> ?
(define (b-templ b)
  (... (button-name b)...
       (button-type b)...
       (button-posntl b)...
       (button-posnbr b)...))
;; Examples
(define B1 (make-button "circle"
                        (overlay (circle 9 "solid" "black") (square 20 "solid" "white"))
                        (make-posn 0 0) (make-posn 20 20)))
(define B2 (make-button "triangle"
                        (overlay (triangle 17 "solid" "black") (square 20 "solid" "white"))
                        (make-posn 20 0) (make-posn 40 20)))
(define B3 (make-button "rectangle"
                        (overlay (rectangle 18 13 "solid" "black") (square 20 "solid" "white"))
                        (make-posn 40 0) (make-posn 60 20)))
(define B4 (make-button "star"
                        (overlay (star 10 "solid" "black") (square 20 "solid" "white"))
                        (make-posn 60 0) (make-posn 80 20)))
(define B5 (make-button "plus"
                        (overlay (text "+" 20 "black") (square 20 "solid" "white"))
                        (make-posn 80 0) (make-posn 100 20)))
(define B6 (make-button "minus"
                        (overlay (text "-" 20 "black") (square 20 "solid" "white"))
                        (make-posn 100 0) (make-posn 120 20)))
(define B7 (make-button "red"
                        (square 20 "solid" "red")
                        (make-posn 120 0) (make-posn 140 20)))
(define B8 (make-button "orange"
                        (square 20 "solid" "orange")
                        (make-posn 140 0) (make-posn 160 20)))
(define B9 (make-button "yellow"
                        (square 20 "solid" "yellow")
                        (make-posn 160 0) (make-posn 180 20)))
(define B10 (make-button "green"
                         (square 20 "solid" "green")
                         (make-posn 180 0) (make-posn 200 20)))
(define B11 (make-button "blue"
                         (square 20 "solid" "blue")
                         (make-posn 200 0) (make-posn 220 20)))
(define B12 (make-button "purple"
                         (square 20 "solid" "purple")
                         (make-posn 220 0) (make-posn 240 20)))
(define BX (make-button "X" (text "empty" 1 "white") (make-posn 0 0) (make-posn 0 0)))

  
;; A Toolbar is a [List-of Buttons]
;; Interpretation: Represents a toolbar of buttons that have different actions and positions
(define TOOLBAR (list B1 B2 B3 B4 B5 B6 B7 B8 B9 B10 B11 B12))
;; A [List-of Buttons] is one of(cons X [List-of X])
;; Interpretation: A list of elements of type X.
;; Examples:
;; ... omitted ...
;; Template:
;; lox-templ : [List-of X] -> ?
(define (lox-templ lox)
  (cond
    [(empty? lox) ...]
    [(cons? lox) (... (first lox) ...
                      (lox-templ (rest lox)) ...)]))

;; fun-paint: FunPaintWorldState -> Image
;; Purpose: Uses big bang to create a FunPaint program


;; Task 3

;;
;;
(define DRAWTOOLBAR
  (beside (button-type B1)
          (button-type B2)
          (button-type B3)
          (button-type B4)
          (button-type B5)
          (button-type B6)
          (button-type B7)
          (button-type B8)
          (button-type B9)
          (button-type B10)
          (button-type B11)
          (button-type B12)))


;;
;;
;(define (draw-full-scene fpws)
;  (above/align "left"
;               DRAWTOOLBAR
;               (place-image (above (draw-mini (placed-shape-shape (fp-ws-placed-shape fpws)))
;                                   (text-mini (placed-shape-shape (fp-ws-placed-shape fpws)))) 
;                            30 30
;                            (draw-placed-shapes (fp-ws-lops fpws) BACKGROUND))))
;;
;;
;;


;; Task 4

(define-struct fpws2 [shape size color placed-shape lops])

;; press-check : Number Number ############ -> Button
;; Purpose: Outputs the button in which the inputted x and y coordinates correspond to
(define (button-check x y lob)
  (cond
    [(empty? lob) BX]
    [(and (<= (posn-x (button-posntl (first lob))) x (posn-x (button-posnbr (first lob))))
          (<= (posn-y (button-posntl (first lob))) y (posn-y (button-posnbr (first lob)))))
     (first lob)]
    [else (button-check x y (rest lob))]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(define (shape-select ws x y event)
  (cond
    [(string=? (button-name (button-check x y TOOLBAR)) "circle")
     (make-fpws2 "circle"
                 (fpws2-size ws)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "triangle")
     (make-fpws2 "triangle"
                 (fpws2-size ws)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "rectangle")
     (make-fpws2 "rectangle"
                 (fpws2-size ws)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "star")
     (make-fpws2 "star"
                 (fpws2-size ws)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [else ws]))

(define (color-select ws x y event)
  (cond
    [(string=? (button-name (button-check x y TOOLBAR)) "red")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "red"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "orange")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "orange"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "yellow")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "yellow"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "green")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "green"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "blue")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "blue"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(string=? (button-name (button-check x y TOOLBAR)) "purple")
     (make-fpws2 (fpws2-shape ws)
                 (fpws2-size ws)
                 "purple"
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [else ws]))


(define (shape-model ws)
  (cond
    [(string=? (fpws2-shape ws) "circle")
     (circle 15 "solid" (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "triangle")
     (triangle 25 "solid" (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "rectangle")
     (rectangle 25 15 "solid" (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "star")
     (star 20 "solid" (fpws2-color ws))]))

(define (make-size ws x y event)
  (cond
    [(string=? (button-name (button-check x y TOOLBAR)) "plus")
     (make-fpws2 (fpws2-shape ws)
                 (+ (fpws2-size ws) 5)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [(and (string=? (button-name (button-check x y TOOLBAR)) "minus")
          (> (fpws2-size ws) 5))
     (make-fpws2 (fpws2-shape ws)
                 (- (fpws2-size ws) 5)
                 (fpws2-color ws)
                 (fpws2-placed-shape ws)
                 (fpws2-lops ws))]
    [else (make-fpws2 (fpws2-shape ws)
                      (fpws2-size ws)
                      (fpws2-color ws)
                      (fpws2-placed-shape ws)
                      (fpws2-lops ws))]))

(define (text-model ws)
  (cond
    [(string=? (fpws2-shape ws) "circle")
     (text (number->string (fpws2-size ws)) 15 "black")]
    [(string=? (fpws2-shape ws) "triangle")
     (text (number->string (fpws2-size ws)) 15 "black")]
    [(string=? (fpws2-shape ws) "rectangle")
     (text (string-append (number->string (+ (fpws2-size ws) 5))
                          "   "
                          (number->string (fpws2-size ws))) 15 "black")]
    [(string=? (fpws2-shape ws) "star")
     (text (number->string (fpws2-size ws)) 15 "black")]))


(define (draw-shape2 ws)
  (cond
    [(string=? (fpws2-shape ws) "circle")
     (circle (fpws2-size ws) "solid" (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "triangle")
     (triangle (fpws2-size ws) "solid" (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "rectangle")
     (rectangle (+ (fpws2-size ws) 5)
                (fpws2-size ws)
                "solid"
                (fpws2-color ws))]
    [(string=? (fpws2-shape ws) "star")
     (star (fpws2-size ws) "solid" (fpws2-color ws))]))

;; -> Image
(define (make-placed-shapes2 ws x y event)
  (place-image (draw-shape2 ws)
               x
               y
               (fpws2-lops ws)))
  

(define (draw-full-scene-final ws)
  (above/align "left"
               DRAWTOOLBAR
               (place-image (above (shape-model ws) (text-model ws))
                            30 30
                            (fpws2-lops ws))))

;; click-check : WorldState X Y Event -> WorldState
(define (click-check ws x y event)
  (cond
    [(mouse=? event "button-down")
     (cond
       [(or (string=? (button-name (button-check x y TOOLBAR)) (button-name B1))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B2))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B3))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B4)))
        (shape-select ws x y event)]
       [(or (string=? (button-name (button-check x y TOOLBAR)) (button-name B5))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B6)))
        (make-size ws x y event)]
       [(or (string=? (button-name (button-check x y TOOLBAR)) (button-name B7))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B8))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B9))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B10))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B11))
            (string=? (button-name (button-check x y TOOLBAR)) (button-name B12)))
        (color-select ws x y event)]
       [(string=? (button-name (button-check x y TOOLBAR)) (button-name BX))
         (make-fpws2 (fpws2-shape ws)
                     (fpws2-size ws)
                     (fpws2-color ws)
                     (draw-shape2 ws)
                     (make-placed-shapes2 ws x y event))])]
    [else ws]))
  


(define (fun-paint fpws)
  (big-bang fpws
    [to-draw draw-full-scene-final]
    [on-mouse click-check]))

(fun-paint (make-fpws2 "rectangle" 30 "red" (rectangle 35 30 "solid" "red") BACKGROUND))