{********************************************}
{*          Objective-SCM for SB3           *}
{*            SuperVars required            *}
{*        Written by Deji in SB-SCR         *}
{********************************************}

CONST
OBJECT_HEADER_SIZE = 0x14
_OBJECT_DTOR = -1@
_OBJECT_GET_TYPE = -2@
_OBJECT_GET_SIZE = -3@
_OBJECT_GET_NUM = -4@
SUPERVAR = string
this = 0@
END
VAR
THIS:array 4 of SUPERVAR
END

//core functions
{}:_object_init
CALL 1@ 0 3@ 4@ 5@
0085: this[_OBJECT_DTOR] = 4@
0085: this[_OBJECT_GET_TYPE] = 1@
0085: this[_OBJECT_GET_SIZE] = 5@
2@ += 1
0085: this[_OBJECT_GET_NUM] = 2@
this += OBJECT_HEADER_SIZE
0006: this[this] = @_object_vtbl
RET 1 this

{}:_object_vtbl
HEX
00000000
@_object_dtor
@_object_gettype
@_object_getsize
@_object_getnum
END

{}:_object_dtor
this -= OBJECT_HEADER_SIZE
8039:   NOT this[_OBJECT_DTOR] == 0
ELSE_GOTO @_object_dtor_end
CALL this[_OBJECT_DTOR] 1 this
{}:_object_dtor_end
CALL @free 1 this
RET 0

{}:_object_gettype
this -= OBJECT_HEADER_SIZE
RET 1 this[_OBJECT_GET_TYPE]

{}:_object_getsize
this -= OBJECT_HEADER_SIZE
RET 1 this[_OBJECT_GET_SIZE]

{}:_object_getnum
this -= OBJECT_HEADER_SIZE
RET 1 this[_OBJECT_GET_NUM]

{}:_object_getbase
WHILE 8039:   NOT this[this] == 0
    0085: this = this[this]
END
RET 1 this 

:GetObjectBase{\__(obj)__}
CALL @_object_getbase 1 this this
RET 1 this

:new{\__[pObj]__(type(),_nNum)__}
{
  Description:
    Similar to new[] in C++. This creates an object or an array of objects of a certain type().
    Emulation of type's can be created using a type() function, for example:
        :type
        ret 3 @constructor_call @destructor_call 32
    In this example, 32 is the type()'s size. Use 0 to not use a constructor/destructor call.
    You can use a constructor function to set a virtual table, but the first VTBL call MUST
    be a pointer to the default VTBL (@_object_vtbl), unless you wish to replace the default
    behaviours (not recommended).
    
    The num parameter is the number of elements of type() to allocate in a new array.
    Equivalent to using: new type[32]; in C++
    
    On success, the script condition result is set to TRUE and the object/array pointer is
    returned.
    On failure, the script condition result is set to FALSE and NULL is returned.
}
CALL 0@ 0 2@ 3@ 4@
0A90: 5@ = 4@ * 1@
5@ += OBJECT_HEADER_SIZE
CALL @malloc 1 5@ 6@
ELSE_GOTO @new_fail
WRITE_MEMORY 6@ 5@ 0 OFF
CALL @_object_init 3 6@ 0@ 1@ 6@
NOT 2@ == 0
ELSE_GOTO @new_end
NOT 1@ == 0
ELSE_GOTO @new_1
CALL @array_walk 2 6@ 2@
GOTO @new_end
{}:new_1
CALL 2@ 1 6@
{}:new_end
RETURN_TRUE
RET 1 6@
{}:new_fail
RET 1 0

:delete{\__(pObj)__}
{
  Description:
    Similar to delete in C++. This deletes an object or an array of objects and frees the
    memory allocated to it.
}
CALL this[_OBJECT_DTOR] 1 this
RET 0

:malloc{\__[pMem]__(nSize)__}
{
  Description:
    Basically just a wrapper for CLEO4's ALLOCATE_MEMORY.
    
    On success, the script condition result is set to TRUE and the memory pointer is
    returned.
    On failure, the script condition result is set to FALSE and NULL is returned. 
}
ALLOCATE_MEMORY 0@ 1@
NOT 1@ == 0
ELSE_GOTO @malloc_fail
RET 1 1@
{}:malloc_fail
PRINTSTRING "ALLOC_ERROR"
RET 1 0

:free{\__(pMem)__}
{
  Description:
    Free's the memory allocated with malloc.
}
NOT 0@ == 0
ELSE_GOTO @_free_END
FREE_MEMORY 0@
{}:_free_END
RET 0

:calloc{\__[pMem]__(nSize,_nCount)__}
{
  Description:
    C-style calloc wrapper for malloc. The allocated memory is initialised to zero.
    
    On success, the script condition result is set to TRUE and the memory pointer is
    returned.
    On failure, the script condition result is set to FALSE and NULL is returned.
}
NOT 1@ == 0
ELSE_GOTO @calloc_1
006A: 0@ *= 1@
{}:calloc_1
CALL @malloc 1 0@ 2@
ELSE_GOTO @calloc_end
WRITE_MEMORY 2@ 0@ 0 OFF
RET 1 2@
{}:calloc_end
RET 1 0

:sizeof{\__[nSize]__(pObj)__}
{
  Description:
    Returns the size of the type(), object or array of objects in bytes.
}
0@ >= 0
ELSE_GOTO @sizeof_1
0085: 1@ = 0@[0@]
CALL 1@(_OBJECT_GET_SIZE,4s) 1 0@ 2@
CALL 1@(_OBJECT_GET_NUM,4s) 1 0@ 3@
006A: 2@ *= 3@
GOTO @sizeof_end
{}:sizeof_1
CALL 0@ 0 0@ 1@ 2@
{}:sizeof_end
RET 1 2@

:count{\__[nCount]__(Obj[])__}
{
  Description:
    Returns the number of objects in an array.
    If used on a single object, the result will be 1.
}
0085: 1@ = this[this]
CALL 1@(_OBJECT_GET_NUM,4s) 1 this 2@
RET 1 2@

:gettype{\__[type()]__(pObj)__}
{
  Description:
    Returns the objects type().
}
0085: 1@ = this[this]
CALL 1@(_OBJECT_GET_TYPE,4s) 1 this 2@
RET 1 2@

:array_walk{\__(Obj[],_Func())__}
{
  Description:
    Iterates over the array and calls the specified method on each object.  
}
CALL @gettype 1 0@ 2@
CALL @sizeof 1 2@ 3@
CALL @sizeof 1 0@ 4@
0A8E: 5@ = 0@ + 4@
0062: 5@ -= 3@
FOR 0@ = 0@ TO 5@ STEP 3@
    CALL 1@ 1 0@
END
RET 0