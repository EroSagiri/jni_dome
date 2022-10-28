use jni::JNIEnv;
use jni::objects::{JClass, JString};
use jni::sys::jstring;

#[no_mangle]
pub extern "system" fn Java_Hello_hello(env: JNIEnv, _jclass: JClass, input: JString) -> jstring {
    let input = env.get_string(input).expect("Couldn't get java string!");
    let output = env.new_string(format!("Hello {}!", input.to_str().unwrap())).expect("Couldn't create java string!");
    return output.into_raw();
}