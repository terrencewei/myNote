from [github gists](https://gist.github.com/terrencewei/95fa6e15047ffa3fec1714aad6fade93)

how to use:
```js
var obj = {
	attr: 1,
};
obj.watch("attr", function (attrKey, oldValue, newValue) {
	console.log("watch attrKey is:" + attrKey);
	console.log("watch oldValue is:" + oldValue);
	console.log("watch newValue is:" + newValue);
	// need return the value you want 'obj.atr1' is when finished the watch logic
	// otherwise the 'obj.atr1' will be 'undefined'
	return newValue;
});
console.log("before obj.attr1 changed, its value is:" + obj.attr);
obj.attr = 2;
console.log("before obj.attr1 changed, its value is:" + obj.attr);
```
in console output:

before obj.attr1 changed, its value is:1

watch attrKey is:attr

watch oldValue is:1

watch newValue is:2

before obj.attr1 changed, its value is:2