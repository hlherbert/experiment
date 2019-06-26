package com.hl.gene.old;

import javax.swing.text.JTextComponent;

// Print command line
public class CmdPrinter {

	enum OutputMode{
		Console,
		StringBuffer;
	}
	
	private OutputMode m_mode = OutputMode.Console;
	private StringBuffer m_cmdBuf=null;
	
	public void SetOutputToConsole()
	{
		m_mode=OutputMode.Console;
	}
	public void SetOutputToStringBuffer(StringBuffer buf)
	{
		m_mode=OutputMode.StringBuffer;
		m_cmdBuf=buf;
	}
	
	private void printCmdConsole(String s)
	{
		System.out.print(s);
	}
	private void printCmdStrBuf(String s)
	{
		if (m_cmdBuf!=null)
		{
			m_cmdBuf.append(s);
		}
	}
	
	public void PrintCmd(String s)
	{
		if (m_mode==OutputMode.Console)
		{
			printCmdConsole(s);
		}
		else if( m_mode==OutputMode.StringBuffer)
		{
			printCmdStrBuf(s);
		}
	}
}
