System.register(['angular2/src/facade/lang', 'angular2/core'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var lang_1, core_1;
    var MyTab;
    return {
        setters:[
            function (lang_1_1) {
                lang_1 = lang_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            MyTab = (function () {
                function MyTab(_ngEl, _renderer) {
                    this._ngEl = _ngEl;
                    this._renderer = _renderer;
                }
                Object.defineProperty(MyTab.prototype, "tab", {
                    set: function (v) {
                        if (lang_1.isString(v)) {
                            this._tab = v;
                            this._toggleTab();
                        }
                    },
                    enumerable: true,
                    configurable: true
                });
                Object.defineProperty(MyTab.prototype, "thisTab", {
                    set: function (v) {
                        this._thisTab = v;
                        this._toggleTab();
                    },
                    enumerable: true,
                    configurable: true
                });
                MyTab.prototype._toggleTab = function () {
                    this._renderer.setElementClass(this._ngEl.nativeElement, "active", this._thisTab == this._tab);
                };
                MyTab = __decorate([
                    core_1.Directive({ selector: '[currentTab]', inputs: ['tab: currentTab', 'thisTab: name'] }), 
                    __metadata('design:paramtypes', [core_1.ElementRef, core_1.Renderer])
                ], MyTab);
                return MyTab;
            }());
            exports_1("MyTab", MyTab);
        }
    }
});
//# sourceMappingURL=tab.directive.js.map