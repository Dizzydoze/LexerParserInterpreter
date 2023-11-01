# LexerParserInterpreter

![LexerParserInterpreter](https://github.com/Dizzydoze/LexerParserInterpreter/assets/106438058/30ddfed8-21b3-485e-b7b7-52be0a9fee81)

- **Description**: A program for translate code into byte-code for a arbitrary language. There are three parts of it, Lexer, Parser and Interpreter.
- **Repo**: https://github.com/Dizzydoze/LexerParserInterpreter
- **Development process**:
    - **Process**: For code translation. First part is Lexer, it translates the code into a list of Tokens, which stores in an hash table contained token ID and token Value. Second part is Parser, it checks the validation of the token list by ensuring the order of the correction of token sequence. The last part is interpreter, it translates all the tokens into simulated byte codes through accumulator and stores all the byte codes into the memory.
    - **Bugs**:  The most frequent bug I encountered stayed in the Parser. In order to check the validation of the the whole assignment, it has been separated into 3 parts, the left part should be validated ID, INT or simply just PLUS TOKENs. Assignment operation is just a single operation sign which is obvious. When it comes to expression, we need to keep tracking whether the expression is ended by checking the EOF TOKEN and PLUS TOKENs  simultaneously, including the validation of each of the TOKEN we encountered. Try to do it step by step instead of rushing into if else logic.
    - **Concepts&Algo**: For this project, the there’s no fancy algorithms, only design, just get familiar with the procedure how the TOKEN being parsed and interpreted into byte code.
- **Test Cases**:
    - **LexerTest**: Provide a series of assignments and try to translate all the characters into TOKEN we defined.
    - **ParserInterpreterTest**: Use the TOKEN list we generated from the LexerTest, let parser check the validation of each of the assignments and pass them to Interpreter, see if we can get a series of byte code out of them.
