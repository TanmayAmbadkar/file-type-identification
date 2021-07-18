/*
** Rexx script to pass notifications to Ringhio
** Created by George Sokianos 
** for the Amiga OS4  v0.7 (16/2/2013)
** contact me at walkero@yahoo.com
** 
*/
parse arg msgtitle'|'msg'|'msgicon
options results
if show('P','RINGHIO') then do
	address RINGHIO

	'REGISTERAPP APP=RexxMSGS ICON="SYS:Documentation/Ringhio/rx_testringhio.rexx" This the notifier from rexx'
	'RINGHIO APP=RexxMSGS CLOSEONDC SCREEN="FRONT" IMG="'msgicon'" TITLE="'msgtitle'" 'msg
end