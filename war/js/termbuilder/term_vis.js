/******************************************
Data visulization and click event handling
*******************************************/

/******************************************
           Global parameters
*******************************************/
var fill = d3.scale.category20();
// var fill = ['#5195C9', '#3982C4', '#1467B5', '#004080', '#112B4D'];
var fontFamily = "Helvetica Neue";

var w = 615,
    h = 240;
var rectHorizontalMargin = 2;
var textBlockPadding = 10;
var selectedClass = [];
var classData = [];

/******************************************
        Prepare Class (word) Data
*******************************************/
classData = [
      "Hello", "world", "normally", "you", "want", "more", "words",
      "than", "this", "Pizza", "Pizza Topping", "Pizza Base", "Other"].map(function(d) {
      return {text: d, size: 20 + Math.random() * 10};
    });

/******************************************
              Main Method
*******************************************/ 

// function initializeTermVis(canvasWidth, canvasHeight) {
//   console.log("initializeTermVis() function run!");
//   w = canvasWidth;
//   h = canvasHeight;
//   console.log('Get Canvas Width' + canvasWidth);
//   d3.layout.cloud().size([w, h])
//       .words(classData)
//       .padding(textBlockPadding)
//       .rotate(0)
//       .font("Impact")
//       .fontSize(function(d) { return d.size; })
//       .on("end", draw)
//       .start();
// }

function initializeTermVis() {
  console.log("initializeTermVis() function run!");
  d3.layout.cloud().size([w, h])
      .words(classData)
      .padding(textBlockPadding)
      .rotate(0)
      .font("Impact")
      .fontSize(function(d) { return d.size; })
      .on("end", draw)
      .start();
}

function clearTermVis() {
  d3.select("#term-vis-svg")
       .remove();
}

/******************************************
              Draw Event
*******************************************/ 
function draw(words) {
  // var temp = d3.select("iframe#__download");
  // console.log(temp);
  console.log("draw() function run!");
  var canvas = d3.select("div#termVisCanvas");
  console.log(canvas);
  canvas.attr('height', 200);
  canvas.append("svg")
      .attr("id", "term-vis-svg")
      .attr("width", w)
      .attr("height", h)
      //Add a onload event to move the rectangles before texts
      // .attr("onload", 'svgInit(e)')
    .append("g")
      .attr("id", "text-group")
      .attr("transform", "translate("+ w/2 +"," + h/2 + ")")
    .selectAll("text")
      .data(words)
    .enter().append("text")
      .style("font-size", function(d) { return d.size + "px"; })
      .style("font-family", fontFamily)
      .style("fill", function(d, i) { return fill(i); })
      .attr("text-anchor", "middle")
      .attr("transform", function(d) {
        return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")";
      })
      .text(function(d) { return d.text; });
  
  //Draw rectangles
  //Prepare parameters
  var rectData = [];
  var svg = d3.select('svg');
  var svgTop = 0;
  var svgLeft = 0;
  svg.each(function() {
    var clientRect = this.getBoundingClientRect();
    svgTop = clientRect.top;
    svgLeft = clientRect.left;
  });

  //Retrieve necessary data
  var texts = d3.selectAll("text");
  texts.each( function(d) {
    var clientRect = this.getBoundingClientRect();
    clientRect.text = d.text;
    clientRect.textSize = d.size;
    rectData.push(clientRect);
  } );

  //Define rectanges
  var rects = svg.append("g")
                .attr('id', 'rect-group')
                .selectAll('rect')
                .data(rectData)
                 .enter()
                 .append("rect")
                 .on('click', click)
                 .on('mouseover', mouseover)
                 .on('mouseout', mouseout);

  //Begin drawing
  rects.attr('id', function(d) {
          return d.text;
        })
        .attr('x', function(d) {
          return d.left - svgLeft - rectHorizontalMargin;
        })
        .attr('y', function(d) {
          return d.top - svgTop;
        })
        .attr('rx', 5)
        .attr('ry', 5)
        .attr('width', function(d) {
          return d.width + rectHorizontalMargin * 2;
        })
        .attr('height', function(d) {
          return d.height;
        })
        .attr('class', 'rect-pristine');
}

/******************************************
              Event Listener
*******************************************/ 
function click() {
  var target = d3.event.target;
  var targetClass = target.getAttribute('class');
  var targetID = target.getAttribute('id');
  if(targetClass == 'rect-selected') {
    target.setAttribute('class', 'rect-mouseover');
    removeFromSelectedClass(targetID);
  } else {
    target.setAttribute('class', 'rect-selected');
    addToSelectedClass(targetID);
  }
}

function mouseover() {
  var target = d3.event.target;
  setcursor("pointer");
  var targetClass = target.getAttribute('class');
  // console.log(targetClass);
  if(targetClass != 'rect-selected') {
    target.setAttribute('class', 'rect-mouseover');
    // console.log(target.getAttribute('class'));
  }
}

function mouseout() {
  var target = d3.event.target;
  var targetClass = target.getAttribute('class');
  setcursor("default");
  if(targetClass == 'rect-mouseover') {
    target.setAttribute('class', 'rect-pristine');
  }
}


/*********************************************
             Utility functions
**********************************************/

function addToSelectedClass(c) {
  selectedClass.push(c);
}

function removeFromSelectedClass(c) {
  var index = selectedClass.indexOf(c);

  if (index > -1) {
      selectedClass.splice(index, 1);
  }
}

function setcursor(cursor)
{
  d3.select("svg").style("cursor", cursor);
}
