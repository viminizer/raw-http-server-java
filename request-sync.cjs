const { promisify } = require('node:util');
const exec = promisify(require('node:child_process').exec);

(async () => {
  const total = 500;
  let count = total;
  const start = performance.now();
  while (count > 0) {
    const { stdout } = await exec(
      `curl -v -X POST http://localhost:4455 -H "Content-Type: application/json" -H "Authorization: Bearer mytoken123" -d '{"user": "test_dev", "action": "raw_server_test"}'`
    );
    console.log(stdout);
    count--;
  }
  const elapsed = performance.now() - start;
  console.log(`\nAll ${total} requests completed in ${elapsed.toFixed(2)} ms`);
})();
