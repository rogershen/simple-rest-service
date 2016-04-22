import {isPresent, isString} from 'angular2/src/facade/lang';
import {
  Directive,
  ElementRef,
  Renderer,
	OnChanges,
	SimpleChange,
	Input
} from 'angular2/core';
import {StringMapWrapper, isListLikeIterable} from 'angular2/src/facade/collection';

@Directive({selector: '[currentTab]', inputs: ['tab: currentTab', 'thisTab: name']})
export class MyTab {
  private _tab: string;
	private _thisTab: string;
  
	constructor(private _ngEl: ElementRef, private _renderer: Renderer) {}

  set tab(v: string) {
    if (isString(v)) {
			this._tab = v;
			this._toggleTab();
    }
  }
	
	set thisTab(v: string) {
		this._thisTab = v;
		this._toggleTab();
  }
	
	private _toggleTab(): void {
		 this._renderer.setElementClass(this._ngEl.nativeElement, "active", this._thisTab == this._tab);
  }
}